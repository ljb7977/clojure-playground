(ns util
  (:require [clojure.java.io :as io]))

(defn read-input [path]
  (->> path
       (io/resource)
       (io/reader)
       (line-seq)))
