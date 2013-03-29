(ns foreclojure-downloader.translator
  (:require [clojure.string :as string]))

(defn- parse-simple-test
  "Parse simple tests of the form (= x y) where possible."
  [test]
  (let [[eq & _ :as parsed-test] (binding [*read-eval* false] (read-string test))]
    (when (and (= (count parsed-test) 3) (or (= eq '=) (= eq '==)))
          (next (re-find #"^(?s)\((==?)\s+(#?\".+\"|'?\(.+\)|\[.+\]|#?\{.+\}|\S+)\s+(#?\".+\"|'?\(.+\)|\[.+\]|#?\{.+\}|\S+)\)$"
                         test)))))

(defn- test->checker
  "Translate a test into a Midje checker.

  Tests of the form (= x y) will be translated to x => y but more complex tests
  will be preserved and checked to be true, e.g. (and (= x y) (= z a)) => true"
  [test]
  (let [normalized-test (string/replace test "\r\n" "\n")]
    (if-let [[equals left right] (parse-simple-test normalized-test)]
      (str "  " left " " equals "> " right)
      (str normalized-test " => true"))))

(defn- checkers
  "Return all checkers for the given problem."
  [problem]
  (str (string/join "\n" (map test->checker (:tests problem))) ")"))

(defn- fact
  "Return a full fact block for a given problem including all checkers."
  [problem]
  (str "(future-fact \"" (:title problem) "\"\n"
       (checkers problem) "\n"))

(defn problem-test
  "Return the full test for a given problem as a String."
  [problem]
  (str "(ns foreclojure-solutions.p" (:number problem) "\n"
       "  (:use midje.sweet))\n"
       "\n"
       "(defn __ []\n"
       "  \"Implement me!\")\n"
       "\n"
       (fact problem)))
