(ns y2020.day8
  (:require [util]))

(def input-val
  (util/read-lines "y2020/day8.txt"))

(defn parse [x]
  (let [[op num] (clojure.string/split x #" ")]
    [(keyword op) (Integer/parseInt num)]))

(defn next-step
  "State machine을 한 step 계산한 결과를 돌려줍니다.
  Input: 0 0 [[:acc 10] [:jmp -1]]
  Output: {:acc 10 :op-id 1}
  "
  [{:keys [acc op-id ops]}]
  (let [[op arg] (nth ops op-id)]
    (case op
      :acc {:acc (+ acc arg) :op-id (inc op-id)}
      :nop {:acc acc :op-id (inc op-id)}
      :jmp {:acc acc :op-id (+ op-id arg)})))

(defn do-until-loop-detected
  "loop이 발견될 때까지 state machine을 실행하고, 루프에 들어갔을 때의 acc 값을 반환합니다.
  Input: [[:acc 10] [:jmp -1]]
  Output: 10
  "
  [ops]
  (loop [acc 0 op-id 0 op-visited? #{}]
    (let [{new-acc :acc new-op-id :op-id} (next-step {:acc acc :op-id op-id :ops ops})]
      (if (op-visited? new-op-id)
        acc
        (recur new-acc new-op-id (conj op-visited? op-id))))))

;reduce 버전
(defn do-until-loop-detected
  [ops]
  (reduce (fn [{:keys [acc op-id op-visited?]} _]
            (let [{new-acc :acc new-op-id :op-id} (next-step {:acc acc :op-id op-id :ops ops})]
              (if (op-visited? new-op-id)
                (reduced acc)
                {:acc new-acc :op-id new-op-id :op-visited? (conj op-visited? op-id)})))
          {:acc 0 :op-id 0 :op-visited? #{}}
          (repeat nil)))

(defn acc-after-program-terminates
  "
  state machine을 계속 실행하면서 프로그램이 마지막 instruction에 도달하였으면 그때의 최종 acc값을 반환하고,
  loop가 발견되었다면 nil을 반환합니다.
  Input: [[:acc 10] [:acc -1] [:nop 10]]
  Output: 9

  Input: [[:acc 10] [:jmp -1] [:nop 10]]
  Output: nil
  "
  [ops]
  (loop [acc 0 op-id 0 op-visited? #{}]
    (let [{new-acc :acc new-op-id :op-id} (next-step {:acc acc :op-id op-id :ops ops})]
      (cond
        (op-visited? new-op-id) nil
        (>= new-op-id (count ops)) new-acc
        :else (recur new-acc new-op-id (conj op-visited? op-id))))))

(defn swap-jmp-and-nop-at
  "ops 벡터에서 idx 위치에 있는 instruction이 jmp면 nop로, nop면 jmp로 바꿔 줍니다.
  Input: 2 [[:acc 10] [:acc -1] [:nop 10]]
  Output: [[:acc 10] [:acc -1] [:jmp 10]]
  "
  [idx ops]
  (let [[op arg] (nth ops idx)
        new-op (case op
                 :jmp :nop
                 :nop :jmp)]
    (assoc ops idx [new-op arg])))

(defn get-indices-of-jmp-or-nop
  "ops 벡터에서 instruction이 jmp나 nop인 것들의 인덱스를 반환합니다.
  Input: [[:acc 10] [:jmp -1] [:nop 10]]
  Output: (1, 2)
  "
  [ops]
  (->> ops
       (map-indexed vector)
       (filter (fn [[idx [op arg]]] (or (= op :nop) (= op :jmp))))
       (map first)))

(defn generate-candidate-ops
  "ops 벡터에서 jmp<->nop를 한번만 뒤바꾼 모든 조합을 만들어냅니다.
  Input: [[:acc 10] [:jmp -1] [:nop 10]]
  Output: [[[:acc 10] [:nop -1] [:nop 10]] [[:acc 10] [:jmp -1] [:jmp 10]]]
  "
  [ops]
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
