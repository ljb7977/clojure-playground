(ns y2021.day2
  (:require util))

(def input-val
  (util/read-lines "y2021/day2.txt"))

(defn parse-line [s]
  (let [[[_ direction amount]] (re-seq #"([a-z]+) (\d+)" s)]
    {:direction (keyword direction)
     :amount (Integer/parseInt amount)}))

(defn process [xs]
  (reduce (fn [{:keys [h depth]} {:keys [direction amount]}]
            (case direction
              :forward {:h (+ h amount) :depth depth}
              :down {:h h :depth (+ depth amount)}
              :up {:h h :depth (- depth amount)}))
          {:h 0 :depth 0}
          xs))

(->> input-val
     (map parse-line)
     process
     vals
     (apply *))