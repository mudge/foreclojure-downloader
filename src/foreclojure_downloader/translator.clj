(ns foreclojure-downloader.translator
  (:require [clojure.string :as string]))

(defn- parse-simple-test
  "Parse simple tests of the form (= x y) where possible."
  [test]
  (let [parsed-test (binding [*read-eval* false] (read-string test))]
    (when (= (count parsed-test) 3)
          (next (re-find #"^(?s)\(==?\s+(#?\".+\"|'?\(.+\)|\[.+\]|#?\{.+\}|\S+)\s+(#?\".+\"|'?\(.+\)|\[.+\]|#?\{.+\}|\S+)\)$"
                         test)))))

(defn- remove-comments
  "Naively remove trailing comments from the given test."
  [test]
  (string/replace test #"(?m)\s+;.+$" ""))

(defn- test->checker
  "Translate a test into a Midje checker.

  Tests of the form (= x y) will be translated to x => y but more complex tests
  will be preserved and checked to be true, e.g. (and (= x y) (= z a)) => true"
  [test]
  (let [indented-test (remove-comments (string/replace test "\r\n" "\n  "))]
    (if-let [[left right] (parse-simple-test indented-test)]
      (str "  " left " => " right)
      (str "  " indented-test " => true"))))

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
