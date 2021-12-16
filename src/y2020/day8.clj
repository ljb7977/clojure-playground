(ns y2020.day8
  (:require [util]))

(def input-val
  (util/read-lines "y2020/day8.txt"))

(defn parse [x]
  (let [[op num] (clojure.string/split x #" ")]
    {:op (keyword op)
     :arg (Integer/parseInt num)}))

(defn next-step
  "State machine을 한 step 계산한 결과를 돌려줍니다.
  Input: {:acc 0 :op-id 0 :ops [{:op :acc :arg 10} {:op :jmp :arg -1}] :history #{}}
  Output: {:acc 10 :op-id 1 :ops [{:op :acc :arg 10} {:op :jmp :arg -1}] :history #{0}}
  "
  [{:keys [acc op-id ops history]}]
  (let [{:keys [op arg]} (nth ops op-id)]
    (case op
      :acc {:acc (+ acc arg)
            :op-id (inc op-id)
            :ops ops
            :history (conj history op-id)}
      :nop {:acc acc
            :op-id (inc op-id)
            :ops ops
            :history (conj history op-id)}
      :jmp {:acc acc
            :op-id (+ op-id arg)
            :ops ops
            :history (conj history op-id)})))

(defn swap-jmp-and-nop-at
  "ops 벡터에서 idx 위치에 있는 instruction이 jmp면 nop로, nop면 jmp로 바꿔 줍니다.
  Input: 2 [[:acc 10] [:acc -1] [:nop 10]]
  Output: [[:acc 10] [:acc -1] [:jmp 10]]
  "
  [idx ops]
  (let [{:keys [op arg]} (nth ops idx)
        new-op (case op
                 :jmp :nop
                 :nop :jmp)]
    (assoc ops idx {:op new-op :arg arg})))

(defn get-indices-of-jmp-or-nop
  "ops 벡터에서 instruction이 jmp나 nop인 것들의 인덱스를 반환합니다.
  Input: [[:acc 10] [:jmp -1] [:nop 10]]
  Output: (1, 2)
  "
  [ops]
  (->> ops
       (map-indexed vector)
       (filter (fn [[idx {:keys [op arg]}]] (or (= op :nop) (= op :jmp))))
       (map first)))

(defn generate-candidate-ops
  "ops 벡터에서 jmp<->nop를 한번만 뒤바꾼 모든 조합을 만들어냅니다.
  Input: [[:acc 10] [:jmp -1] [:nop 10]]
  Output: [[[:acc 10] [:nop -1] [:nop 10]] [[:acc 10] [:jmp -1] [:jmp 10]]]
  "
  [ops]
  (for [idx (get-indices-of-jmp-or-nop ops)] (swap-jmp-and-nop-at idx ops)))

(defn loop-detected? [{:keys [op-id history]}]
  (contains? history op-id))

(defn reached-end? [{:keys [op-id ops]}]
  (>= op-id (count ops)))

(defn not-loop-detected-and-not-reached-end? [state]
  (and
    (not (loop-detected? state))
    (not (reached-end? state))))

(defn get-acc-if-reached-end [{:keys [acc op-id ops history] :as state}]
  (cond
    (loop-detected? state) nil
    (reached-end? state) acc))

(defn acc-after-program-terminates [ops]
  (->>
    (#(iterate next-step {:acc 0 :op-id 0 :ops ops :history #{}}))
    (drop-while not-loop-detected-and-not-reached-end?)
    first
    get-acc-if-reached-end))

(comment
  ; Part 1
  (->> input-val
       (map parse)
       (#(iterate next-step {:acc 0 :op-id 0 :ops % :history #{}}))
       (drop-while loop-detected?)
       first
       :acc)

  ;Part 2
  (->> input-val
       (mapv parse)
       generate-candidate-ops
       (keep acc-after-program-terminates)
       first))
