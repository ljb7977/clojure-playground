(ns util
  (:require [clojure.java.io :as io]))

(defn read-lines [path]
  (->> path
       io/resource
       io/reader
       line-seq))
