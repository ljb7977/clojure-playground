(ns y2022.day2
  (:require [clojure.core.match :refer [match]]))

(def input (slurp "src/y2022/day2.txt"))

(def abc->rps
  {"A" :rock
   "B" :paper
   "C" :scissors})

(def xyz->rps
  {"X" :rock
   "Y" :paper
   "Z" :scissors})

(def rps->score
  {:rock 1
   :paper 2
   :scissors 3})

(defn parse [input]
  (->> (clojure.string/split-lines input)
       (map #(clojure.string/split % #" "))))

(defn round-score [you me]
  (match [you me]
    [:rock :rock] 3
    [:rock :scissors] 0
    [:rock :paper] 6
    [:paper :paper] 3
    [:paper :scissors] 6
    [:paper :rock] 0
    [:scissors :scissors] 3
    [:scissors :rock] 6
    [:scissors :paper] 0))

(defn calc-score [[you me]]
  (let [score (round-score you me)]
    (+ score (rps->score me))))

(comment
  ;; Part 1
  (let [parsed (parse input)]
    (->> parsed
         (map (fn [[x1 x2]]
                [(abc->rps x1) (xyz->rps x2)]))
         (map calc-score)
         (apply +)))
  := 12772
  :rcf)

(def xyz->win
  {"X" :lose
   "Y" :draw
   "Z" :win})

(defn make-rps [you result]
  (match [you result]
    [:rock :win] :paper
    [:rock :draw] :rock
    [:rock :lose] :scissors
    [:scissors :win] :rock
    [:scissors :draw] :scissors
    [:scissors :lose] :paper
    [:paper :win] :scissors
    [:paper :draw] :paper
    [:paper :lose] :rock))

(comment
  (->> (parse input)
       (map (fn [[x1 x2]]
              [(abc->rps x1) (xyz->win x2)]))
       (map (fn [[you result]]
              (let [me (make-rps you result)]
                [you me])))
       (map calc-score)
       (apply +))
  := 11618)
