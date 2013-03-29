(ns foreclojure-downloader.writer
  (:require [foreclojure-downloader.translator :as translator]))

(defn- file-name
  "Return the file name for the given problem."
  [problem]
  (str "src/foreclojure_solutions/p" (:number problem) ".clj"))

(defn write-problem
  "Write the given problem to disk."
  [problem]
  (let [dir (java.io.File. "src/foreclojure_solutions")]
    (when-not (.exists dir) (.mkdirs dir))
    (spit (file-name problem) (translator/problem-test problem))
    problem))

