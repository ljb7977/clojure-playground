(ns y2022.day9
  (:require [clojure.string :as str]))

(def input (slurp "src/y2022/day9.txt"))

(def sample-input "R 5\nU 8\nL 8\nD 3\nR 17\nD 10\nL 25\nU 20")

(defn parse-input [input]
  (->> (str/split-lines input)
       (map #(str/split % #" "))
       (map (fn [[direction amount]] [direction (parse-long amount)]))))

(defn next-tail-position [{head-x :x head-y :y :as new-head}
                          {tail-x :x tail-y :y :as tail}]
  ;(prn "new-head" new-head)
  ;(prn "tail" tail)
  (let [dx (- head-x tail-x)
        dy (- head-y tail-y)]
    (cond
      (and (<= -1 dx 1) (<= -1 dy 1)) tail
      
      (and (= dx 0) (= dy 2)) {:x tail-x :y (inc tail-y)}
      (and (= dx 0) (= dy -2)) {:x tail-x :y (dec tail-y)}
      (and (= dx 2) (= dy 0)) {:x (inc tail-x) :y tail-y}
      (and (= dx -2) (= dy 0)) {:x (dec tail-x) :y tail-y}
      
      (or (and (>= dx 1) (= dy 2))
          (and (= dx 2) (>= dy 1))) {:x (inc tail-x) :y (inc tail-y)}
      
      (or (and (>= dx 1) (= dy -2))
          (and (= dx 2) (<= dy -1))) {:x (inc tail-x) :y (dec tail-y)}
      
      (or (and (<= dx -1) (= dy 2))
          (and (= dx -2) (>= dy 1))) {:x (dec tail-x) :y (inc tail-y)}
      
      (or (and (<= dx -1) (= dy -2))
          (and (= dx -2) (<= dy -1))) {:x (dec tail-x) :y (dec tail-y)})))


(defn get-new-head [{:keys [x y] :as head} direction]
  (case direction
    "U" {:x x :y (inc y)}
    "D" {:x x :y (dec y)}
    "L" {:x (dec x) :y y}
    "R" {:x (inc x) :y y}))

(defn process-step [{:keys [visited head tail] :as acc} direction]
  (let [new-head (get-new-head head direction)
        new-tail (next-tail-position new-head tail)]
    {:visited (conj visited new-tail)
     :head new-head
     :tail new-tail}))

(defn process-step-2 [{:keys [rope visited]} direction]
  (let [new-head (get-new-head (first rope) direction)
        new-rope (reduce (fn [acc val]
                           (let [last-new-coord (last acc)]
                             (conj acc (next-tail-position last-new-coord val))))
                         [new-head]
                         (rest rope))
        new-tail (last new-rope)]
    {:visited (conj visited new-tail)
     :rope new-rope}))

(comment
  ;; Part 1
  (let [directions (->> (parse-input input)
                        (map (fn [[direction amount]] (repeat amount direction)))
                        (apply concat))]
    (-> (reduce process-step
                {:head {:x 0 :y 0}
                 :tail {:x 0 :y 0}
                 :visited #{{:x 0 :y 0}}}
                directions)
        :visited
        count))
  := 5878

  (let [directions (->> (parse-input input)
                        (map (fn [[direction amount]] (repeat amount direction)))
                        (apply concat))]
    (-> (reduce process-step-2
                {:rope (vec (repeat 10 {:x 0 :y 0}))
                 :visited #{{:x 0 :y 0}}}
                directions)
        :visited
        count))
  := 2405)
