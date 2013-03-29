(ns foreclojure-downloader.writer
  (:require [foreclojure-downloader.translator :as translator]))

(defn- file-name
  "Return the file name for the given problem and path."
  [problem path]
  (java.io.File. path (str "p" (:number problem) ".clj")))

(defn write-problem
  "Write the given problem to the given path"
  [problem path]
  (let [dir (java.io.File. path)]
    (when-not (.exists dir) (.mkdirs dir))
    (spit (file-name problem path) (translator/problem-test problem))
    problem))

