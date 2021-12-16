(ns y2018.day7
  (:require util))

(def input-val
  (util/read-lines "y2018/day7-example.txt"))

(defn parse-one-line
  "Input: Step C must be finished before step A can begin.
  Output: {:precond C :result A}
  "
  [s]
  (let [[[_ precond result]] (re-seq #"Step (.) must be finished before step (.) can begin\." s)]
    {:precond      (first precond)
     :removed-keys (first result)}))

(defn ->graph
  "Input: [{:precond \\C, :result \\A}
           {:precond \\C, :result \\F}
           {:precond \\A, :result \\B}
           {:precond \\A, :result \\D}
           {:precond \\B, :result \\E}
           {:precond \\D, :result \\E}
           {:precond \\F, :result \\E}]
  Output: {\\A #{\\C}, \\B #{\\A}, \\C #{}, \\D #{\\A}, \\E #{\\B \\D \\F}, \\F #{\\C}}"
  [xs]
  (let [chars (->> xs
                  (map vals)
                  flatten
                  set)]
    (reduce (fn [acc {:keys [precond removed-keys]}]
              (update acc removed-keys #(conj % precond)))
            (zipmap chars (repeat #{}))
            xs)))

(defn parse [lines]
  (->> lines
       (map parse-one-line)
       ->graph))

(defn get-keys-where-value-is-empty
  "empty value를 가진 key들을 모두 반환합니다.
  Input: {A #{}, B #{C A}, C #{}}
  Output: (A C)"
  [m]
  (->> m
       (filter #(empty? (val %)))
       (map key)))

(defn remove-value
  "맵의 value인 set들에서 특정 value를 모두 제거합니다.
  Input: {A #{C}, B #{C}, C #{}}, C
  Output: {A #{}, B #{}, C #{}}
  "
  [m value]
  (->> m
       (map (fn [[k v]] [k (disj v value)]))
       (into {})))

(defn process-one-step
  "Input: {:result [] :graph {A #{C}, B #{A}, C #{}, D #{A}, E #{B D F}, F #{C}}}
  Output: {:result [C] :graph {A #{}, B {#A}, D #{A}, E #{B D F}, F #{}}}
  "
  [{:keys [removed-keys graph] :as all}]
  (if
    (empty? graph)
    all
    (let [key-to-remove (->> graph
                             get-keys-where-value-is-empty
                             (apply min-key int))]
      {:removed-keys (conj removed-keys key-to-remove)
       :graph        (-> graph
                         (dissoc key-to-remove)
                         (remove-value key-to-remove))})))

;(process-one-step {:result [], :graph {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}}})

(defn find-idle-worker [workers]
  (->> workers
       (keep-indexed #(if (nil? %2) %1))
       first))
;(find-idle-worker [1 nil 2 3 nil])

;worker: {:job \A :remaining 5}
(defn process-workers [workers]
  (mapv (fn [{:keys [job remaining]}]
          (if (= 1 remaining)
            nil
            {:job job :remaining (dec remaining)})) workers))
(process-workers [{:job \A :remaining 2} {:job \B :remaining 1}])

; 미완성.
; 로직:
; 1. 모든 워커들에 대해 1초식 진행시켜준다.
; 2. 끝난 워커가 있으면, 그 워커를 회수한다. (nil로 변경)
; 3. 처리 완료된 알파벳들을 done 리스트에 추가한다.
; 4. 그래프에서 그 알파벳들을 제거한다.
; 5. 넣을 수 있는 만큼 새 알파벳을 워커에 넣는다.
; 이하 반복
;(defn process-one-step-with-workers [{:keys [result graph workers] :as all}]
;  (if
;    (and (every? nil? workers) (empty? graph)) all
;    (let [keys-with-empty-list (get-keys-where-value-is-empty-list graph)
;          min-key-with-empty-list (apply min-key int keys-with-empty-list)
;          workers (process-workers workers)
;          idle-worker-id (find-idle-worker workers)]
;      (if (not (nil? idle-worker-id)))
;      (-> graph
;          (dissoc min-key-with-empty-list)
;          (remove-value-from-all-entry min-key-with-empty-list)
;          ((fn [g]
;             {:result (conj result min-key-with-empty-list) :graph g}))))))

(defn get-result-as-string [{result :removed-keys}]
  (apply str result))

(comment
  ; Part 1
  (->> input-val
       parse
       (#(iterate process-one-step {:removed-keys [] :graph %}))
       (drop-while #(not (empty? (:graph %))))
       first
       get-result-as-string))
