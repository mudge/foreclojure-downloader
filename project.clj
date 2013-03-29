(defproject foreclojure-downloader "0.1.0-SNAPSHOT"
  :description "Downloader for 4clojure problems"
  :url "https://github.com/mudge/foreclojure-downloader"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.0"]
                 [cheshire "5.0.2"]
                 [midje "1.5.1"]]
  :plugins [[lein-midje "3.0.0"] [lein-kibit "0.0.8"]]
  :main foreclojure-downloader.core)
