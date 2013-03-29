(ns foreclojure-downloader.core
  (:require [foreclojure-downloader.api :as api]
            [foreclojure-downloader.writer :as writer]))

(defn- problems
  "All problems currently on 4clojure."
  []
  (filter identity (pmap api/problem (range 1 200))))

(defn -main []
  (doseq [problem (problems)]
    (println "Writing problem" (:number problem) (:title problem))
    (writer/write-problem problem)))
