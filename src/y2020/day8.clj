(ns y2020.day8
  (:require [util]))

(def input-val
  (util/read-lines "y2020/day8.txt"))

(defn parse [x]
  (let [[op num] (clojure.string/split x #" ")]
    {:op (keyword op)
     :arg (parse-long num)}))

(defn update-status
  "이 state machine의 :status를 다음 규칙에 맞게 업데이트합니다
  loop에 빠졌으면 -> :loop
  마지막 operation에 도달했으면 -> :terminated
  그 외의 경우 -> 이전 status 보존
  "
  [{:keys [op-id history ops] :as state}]
  (cond
    (contains? history op-id) (assoc state :status :loop)
    (>= op-id (count ops)) (assoc state :status :terminated)
    :else state))

(defn apply-operation
  "
  Input: {:acc 0 :op-id 0 :ops [{:op :acc :arg 10} {:op :jmp :arg -1}] :history #{}}
  Output: {:acc 10 :op-id 1 :ops [{:op :acc :arg 10} {:op :jmp :arg -1}] :history #{0}}
  "
  [{:keys [acc op-id ops history] :as state}]
  (let [{:keys [op arg]} (nth ops op-id)]
    (case op
      :acc (-> state
               (assoc :acc (+ acc arg))
               (assoc :op-id (inc op-id))
               (assoc :history (conj history op-id)))
      :nop (-> state
               (assoc :op-id (inc op-id))
               (assoc :history (conj history op-id)))
      :jmp (-> state
               (assoc :op-id (+ op-id arg))
               (assoc :history (conj history op-id))))))

(defn next-step
  "State machine을 한 step 진행시킨 결과를 돌려줍니다."
  [state]
  (-> state
      apply-operation
      update-status))

(defn swap-jmp-and-nop-at
  "ops 벡터에서 idx 위치에 있는 instruction이 jmp면 nop로, nop면 jmp로 바꿔 줍니다.
  Input: 2 [{:op :acc
             :arg 10}
            {:op :acc
             :arg -1}
            {:op :nop
             :arg 10}]
  Output: [{:op :acc
            :arg 10}
           {:op :acc
            :arg -1}
           {:op :jmp
            :arg 10}]
  "
  [idx ops]
  (let [{:keys [op arg]} (nth ops idx)
        new-op (case op
                 :jmp :nop
                 :nop :jmp)]
    (assoc ops idx {:op new-op :arg arg})))

(defn get-indices-of-jmp-or-nop
  "ops 벡터에서 instruction이 jmp나 nop인 것들의 인덱스를 반환합니다.
  Input: [{:op :acc :arg 10} {:op :jmp :arg -1} {:op :nop :arg 10}]
  Output: (1 2)
  "
  [ops]
  (->> ops
       (map-indexed vector)
       (filter (fn [[idx {:keys [op arg]}]] (or (= op :nop) (= op :jmp))))
       (map first)))

(defn generate-candidate-ops
  "ops 벡터에서 jmp<->nop를 한번만 뒤바꾼 모든 조합을 만들어냅니다.
  Input: [{:op :acc :arg 10} {:op :jmp :arg -1} {:op :nop :arg 10}]
  Output: [[{:op :acc :arg 10} {:op :nop :arg -1} {:op :nop :arg 10}]
           [{:op :acc :arg 10} {:op :jmp :arg -1} {:op :jmp :arg 10}]]
  "
  [ops]
  (for [idx (get-indices-of-jmp-or-nop ops)] (swap-jmp-and-nop-at idx ops)))

(defn get-acc-if-reached-end [{:keys [acc status]}]
  (cond
    (= status :terminated) acc
    (= status :loop) nil))

(defn initialize [ops]
  {:acc 0
   :op-id 0
   :ops ops
   :history #{}
   :status :running}) ; :status의 도메인: #{:running :loop :terminated}

(defn acc-after-program-terminates [ops]
  (->> ops
       initialize
       (iterate next-step)
       (drop-while (fn [{status :status}] (and (not= status :loop) (not= status :terminated))))
       first
       get-acc-if-reached-end))

(comment
  ; Part 1
  (->> input-val
       (mapv parse)
       initialize
       (iterate next-step)
       (drop-while #(not= (:status %) :loop))
       first
       :acc); 2080

  ;Part 2
  (->> input-val
       (mapv parse)
       generate-candidate-ops
       (keep acc-after-program-terminates)
       first)) ; 2477
