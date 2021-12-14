(ns y2020.day8
  (:require [util]))

(def input-val
  (util/read-lines "y2020/day8.txt"))

(defn parse [x]
  (let [[op num] (clojure.string/split x #" ")]
    [(keyword op) (Integer/parseInt num)]))

(defn state-machine-one-step [{:keys [acc op-id ops]}]
  (let [[op arg] (nth ops op-id)]
    (case op
      :acc {:acc (+ acc arg) :next-op-id (inc op-id)}
      :nop {:acc acc :next-op-id (inc op-id)}
      :jmp {:acc acc :next-op-id (+ op-id arg)})))

(defn do-until-loop-detected [ops]
  (loop [acc 0 op-id 0 op-visited? #{}]
    (let [{new-acc :acc next-op-id :next-op-id} (state-machine-one-step {:acc acc :op-id op-id :ops ops})]
      (if (op-visited? next-op-id)
        acc
        (recur new-acc next-op-id (conj op-visited? op-id))))))

(comment (let [ops (->> input-val
                        (map parse))]
           (do-until-loop-detected ops)))
