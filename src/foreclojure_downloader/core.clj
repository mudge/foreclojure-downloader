(ns foreclojure-downloader.core
  (:require [clojure.string :as string])
  (:require [clojure.core.reducers :as reducers])
  (:require [clj-http.client :as client])
  (:require [cheshire.core :as cheshire]))

(defn download-problem [n]
  "Download the given problem number from 4clojure."
  (let [url (str "http://www.4clojure.com/api/problem/" n)
        response (client/get url {:throw-exceptions false})]
    (when (= 200 (:status response))
      (cheshire/parse-string (:body response) true))))

(defn convert-newlines [string]
  (string/replace string "\r\n" "\n"))

(defn parse-test [test]
  "Take a String test and parse out the equality operator and two sides.

  Do this with a regular expression rather than read-string in an attempt
  to preserve the original test code: e.g. reading #(* % 2) with
  read-string produces (fn* [pn_1234#] (* pn_1234# 2))."
  (if-let [matches (re-find #"^(?s)\((==?)\s+(#?\".+\"|'?\(.+\)|\[.+\]|#?\{.+\}|\S+)\s+(#?\".+\"|'?\(.+\)|\[.+\]|#?\{.+\}|\S+)\)$"
                            (convert-newlines test))]
    matches
    ["" "=" (convert-newlines test) true]))

(defn arrow [test]
  "Return the appropriate checker for the given test."
  (let [equals (second (parse-test test))]
    (str equals ">")))

(defn lhs [test]
  "Return the left-hand side of the given test."
  (nth (parse-test test) 2))

(defn rhs [test]
  "Return the left-hand side of the given test."
  (last (parse-test test)))

(defn checker [test]
  "Return the full checker for the given test."
  (str "  " (lhs test) " " (arrow test) " " (rhs test)))

(defn checkers [problem]
  "Return all checkers for the given problem."
  (str (string/join "\n" (map checker (:tests problem))) ")"))

(defn fact [problem]
  "Return a full fact block for a given problem including all checkers."
  (str "(future-fact \"" (:title problem) "\"\n" (checkers problem) "\n"))

(defn problem-file-content [n problem]
  "Return the full test file content for a given problem."
  (str "(ns foreclojure-solutions.p" n "\n"
       "  (:use midje.sweet))\n"
       "\n"
       "(defn __ []\n"
       "  \"Implement me!\")\n"
       "\n"
       (fact problem)))

(defn problem-file-name [n]
  (str "src/foreclojure_solutions/p" n ".clj"))

(defn write-problem [n]
  (when-let [problem (download-problem n)]
    (.mkdirs (java.io.File. "src/foreclojure_solutions"))
    (spit (problem-file-name n) (problem-file-content n problem)))
    n)

(defn -main []
  (into [] (reducers/map write-problem (range 1 200))))
