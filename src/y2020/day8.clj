(ns y2020.day8
  (:require [util]))

(def input-val
  (util/read-lines "y2020/day8.txt"))

(defn parse [x]
  (let [[op num] (clojure.string/split x #" ")]
    [(keyword op) (Integer/parseInt num)]))

(defn next-step [{:keys [acc op-id ops]}]
  (let [[op arg] (nth ops op-id)]
    (case op
      :acc {:acc (+ acc arg) :op-id (inc op-id)}
      :nop {:acc acc :op-id (inc op-id)}
      :jmp {:acc acc :op-id (+ op-id arg)})))

(defn do-until-loop-detected [ops]
  (loop [acc 0 op-id 0 op-visited? #{}]
    (let [{new-acc :acc new-op-id :op-id} (next-step {:acc acc :op-id op-id :ops ops})]
      (if (op-visited? new-op-id)
        acc
        (recur new-acc new-op-id (conj op-visited? op-id))))))

(comment
  ; Part 1
  (let [ops (->> input-val
                 (map parse))]
    (do-until-loop-detected ops)))
