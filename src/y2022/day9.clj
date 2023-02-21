(ns y2022.day9
  (:require [clojure.string :as str]))

(def input (slurp "src/y2022/day9.txt"))

(defn next-tail-position [{head-x :x head-y :y :as new-head}
                          {tail-x :x tail-y :y :as tail}]
  (let [dx (- head-x tail-x)
        dy (- head-y tail-y)]
    (cond
      (and (<= -1 dx 1) (<= -1 dy 1)) tail
      
      (and (= dx 0) (= dy 2)) {:x tail-x :y (inc tail-y)}
      (and (= dx 0) (= dy -2)) {:x tail-x :y (dec tail-y)}
      (and (= dx 2) (= dy 0)) {:x (inc tail-x) :y tail-y}
      (and (= dx -2) (= dy 0)) {:x (dec tail-x) :y tail-y}
      
      (or (and (= dx 1) (= dy 2))
          (and (= dx 2) (= dy 1))) {:x (inc tail-x) :y (inc tail-y)}
      
      (or (and (= dx 1) (= dy -2))
          (and (= dx 2) (= dy -1))) {:x (inc tail-x) :y (dec tail-y)}
      
      (or (and (= dx -1) (= dy 2))
          (and (= dx -2) (= dy 1))) {:x (dec tail-x) :y (inc tail-y)}
      
      (or (and (= dx -1) (= dy -2))
          (and (= dx -2) (= dy -1))) {:x (dec tail-x) :y (dec tail-y)})))

(defn process-step [{:keys [visited head tail] :as acc} direction]
  (let [{head-x :x head-y :y} head
        new-head (case direction
                   "U" {:x head-x :y (inc head-y)}
                   "D" {:x head-x :y (dec head-y)}
                   "L" {:x (dec head-x) :y head-y}
                   "R" {:x (inc head-x) :y head-y})
        {new-tail-x :x
         new-tail-y :y
         :as new-tail} (next-tail-position new-head tail)]
    {:visited (conj visited [new-tail-x new-tail-y])
     :head new-head
     :tail new-tail}))

(comment
  ;; Part 1
  (let [parsed-input (->> (str/split-lines input)
                          (map #(str/split % #" "))
                          (map (fn [[direction amount]] [direction (parse-long amount)])))
        directions (->> parsed-input
                        (map (fn [[direction amount]] (repeat amount direction)))
                        (apply concat))]
    (-> (reduce process-step {:head {:x 0 :y 0}
                              :tail {:x 0 :y 0}
                              :visited #{[0 0]}} directions)
        :visited
        count))
  := 5878)
