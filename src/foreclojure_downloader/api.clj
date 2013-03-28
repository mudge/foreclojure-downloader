(ns foreclojure-downloader.api
  (:require [clj-http.client :as client])
  (:use [slingshot.slingshot :only [try+]]))

(defn- problem-url
  "Return the 4clojure URL for a given problem number."
  [n]
  (str "http://www.4clojure.com/api/problem/" n))

(defn problem
  "Returns a numbered problem from 4clojure as a map."
  [n]
  (try+ (assoc (:body (client/get (problem-url n) {:as :json})) :number n)
        (catch [:status 404] _)))

