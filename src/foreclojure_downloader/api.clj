(ns foreclojure-downloader.api
  (:require [clj-http.client :as client])
  (:use [slingshot.slingshot :only [try+]]))

(defn- problem-url
  "Return the 4clojure URL for a given problem number."
  [n]
  (str "http://www.4clojure.com/api/problem/" n))

(defn problem
  "Returns a numbered problem from 4clojure as a map.

  Example:
  user> (problem 1)

  {:restricted [],
   :title \"Nothing but the Truth\",
   :times-solved 1432,
   :difficulty \"Elementary\",
   :scores {:16 1, :7 3, :33 1, :4 915, :5 44},
   :tests [\"(= __ true)\"],
   :user \"dbyrne\",
   :number 1,
   :description \"This is a clojure form. Enter a value which will make the form
                  evaluate to true.  Don't over think it!  If you are confused,
                  see the <a href='/directions'>getting started</a> page.
                  Hint: true is equal to true.\",
    :tags []}"
  [n]
  (try+ (assoc (:body (client/get (problem-url n) {:as :json})) :number n)
        (catch [:status 404] _)))

