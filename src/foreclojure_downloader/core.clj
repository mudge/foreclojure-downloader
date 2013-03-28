(ns foreclojure-downloader.core
  (:require [foreclojure-downloader.api :as api]
            [foreclojure-downloader.writer :as writer]))

(defn- problems
  []
  (pmap api/problem (range 1 200)))

(defn -main []
  (do
    (println "Downloading problems...")
    (pmap writer/write-problem (problems))
    nil))
