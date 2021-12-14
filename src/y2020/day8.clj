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

(defn acc-after-program-terminates [ops]
  (loop [acc 0 op-id 0 op-visited? #{}]
    (let [{new-acc :acc new-op-id :op-id} (next-step {:acc acc :op-id op-id :ops ops})]
      (cond
        (op-visited? new-op-id) nil
        (>= new-op-id (count ops)) new-acc
        :else (recur new-acc new-op-id (conj op-visited? op-id))))))

(defn swap-jmp-and-nop-at [idx ops]
  (let [[op arg] (nth ops idx)
        new-op (case op
                 :jmp :nop
                 :nop :jmp)]
    (assoc ops idx [new-op arg])))

(defn get-indices-of-jmp-or-nop [ops]
  (->> ops
       (map-indexed vector)
       (filter (fn [[idx [op arg]]] (or (= op :nop) (= op :jmp))))
       (map first)))

(defn generate-candidate-ops [ops]
  (let [jmp-or-nop-indices (get-indices-of-jmp-or-nop ops)]
    (for [idx jmp-or-nop-indices] (swap-jmp-and-nop-at idx ops))))

(comment
  ; Part 1
  (->> input-val
       (map parse)
       do-until-loop-detected)

  ;Part 2
  (->> input-val
       (map parse)
       vec
       generate-candidate-ops
       (keep acc-after-program-terminates)
       first))
