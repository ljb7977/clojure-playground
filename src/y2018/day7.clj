(ns y2018.day7
  (:require util))

(def input-val
  (util/read-lines "y2018/day7.txt"))

(defn parse-one-line
  "Input: Step C must be finished before step A can begin.
  Output: {:precond C :result A}
  "
  [s]
  (let [[[_ precond result]] (re-seq #"Step (.) must be finished before step (.) can begin\." s)]
    {:precond      (first precond)
     :result (first result)}))

(defn ->graph
  "Input: [{:precond C, :result A}
           {:precond C, :result F}
           {:precond A, :result B}
           {:precond A, :result D}
           {:precond B, :result E}
           {:precond D, :result E}
           {:precond F, :result E}]
  Output: {A #{C}, B #{A}, C #{}, D #{A}, E #{B D F}, F #{C}}"
  [xs]
  (let [chars (->> xs
                   (map vals)
                   (apply concat)
                   set)]
    (reduce (fn [acc {:keys [precond result]}]
              (update acc result #(conj % precond)))
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

(defn remove-values
  "맵의 value인 set들에서 특정 value들을 모두 제거합니다.
  Input: {A #{C}, B #{C}, C #{}}, C
  Output: {A #{}, B #{}, C #{}}
  "
  [m values]
  (->> m
       (map (fn [[k v]] [k (apply disj v values)]))
       (into {})))

(defn remove-jobs-from-graph
  "주어진 graph에서 jobs로 주어지는 모든 알파벳을 제거합니다.
  Input: {A #{C}, B #{A}, C #{}, D #{A}, E #{B D F}, F #{C}}, [C]
  Output: {A #{}, B #{A}, D #{A}, E #{B D F}, F #{}}

  Input: {A #{C}, B #{A}, C #{}, D #{A}, E #{B D F}, F #{C}}, [A C]
  Output: {B #{}, D #{}, E #{B D F}, F #{}}
  "
  [graph jobs]
  (as-> graph m
        (apply dissoc m jobs)
        (remove-values m jobs)))

(defn process-one-step
  "Input: {:removed-keys [] :graph {A #{C}, B #{A}, C #{}, D #{A}, E #{B D F}, F #{C}}}
  Output: {:removed-keys [C] :graph {A #{}, B #{A}, D #{A}, E #{B D F}, F #{}}}
  "
  [{:keys [removed-keys graph] :as all}]
  (if
    (empty? graph)
    all
    (let [key-to-remove (->> graph
                             get-keys-where-value-is-empty
                             (apply min-key int))]
      {:removed-keys (conj removed-keys key-to-remove)
       :graph  (remove-jobs-from-graph graph [key-to-remove])})))
;(process-one-step {:result [], :graph {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}}})

; 로직:
; 1. 모든 워커들에 대해 1초식 진행시켜준다.
; workers: 알파벳 -> 남은 시간의 map
; == (fmap dec workers)
; map comprehension이 있다면 더 좋겠군요...
(defn process-workers-one-step
  "Input: {A 5, B 1}
  Output: {A 4, B 0}"
  [workers]
  (->> workers
       (map (fn [[job remaining]] [job (dec remaining)]))
       (into {})))

; 2. 끝난 워커가 있으면, 그 job을 회수한다.
(defn reap-finished-job
  "Input: {A 0, B 1}
  Output: {:workers {B 1} :finished-jobs (A)}"
  [workers]
  (let [{finished-workers true
         ongoing-workers false} (group-by #(= 0 (val %)) workers)]
    {:finished-jobs (keys finished-workers)
     :workers (into {} ongoing-workers)}))

(defn get-duration-of-job
  "알파벳 job이 들어오면 그 job을 완료하는데 걸리는 시간을 구합니다.
  즉, A -> 61, B -> 62, ..., Z -> 86"
  [job]
  (+ 61 (- (int job) (int \A))))

(defn assign-job-to-workers
  "worker 리스트에 새로운 job을 추가합니다.
  Input: {A 10, B 1}, Z
  Output: {Z 86, A 10, B 1}
  "
  [workers job]
  (assoc workers job (get-duration-of-job job)))
;(assign-job-to-workers {\A 10 \B 1} \Z)

(defn job-already-working?
  "주어진 job이 이미 처리되고 있는지 확인합니다
  Input: {A 10, B 1}, A
  Output: true
  "
  [workers job]
  (contains? workers job))

(defn assign-jobs-to-workers
  "현재 graph에서 처리할 수 있는 job들을 최대한 worker에 할당합니다."
  [workers graph]
  (let [assignable-jobs (->> graph
                             get-keys-where-value-is-empty
                             (sort compare))]
    (reduce (fn [acc val]
              (cond
                (= (count acc) 5) (reduced acc)
                (job-already-working? acc val) acc
                :else (assign-job-to-workers acc val)))
            workers
            assignable-jobs)))

; 3. 처리 완료된 알파벳들을 done 리스트에 추가한다.
; 4. 그래프에서 그 알파벳들을 제거한다.
; 5. 넣을 수 있는 만큼 새 알파벳을 워커에 넣는다.
(defn process-step-with-workers
  "worker들을 이용한 한 스텝을 진행합니다.
  먼저 주어진 workers에서 처리 완료된 job들을 모두 수거하고, finished-jobs에 추가합니다.
  그리고 수거한 job에 해당하는 알파벳들을 그래프에서 제거해 줍니다.
  그 다음엔 새 그래프에서 이제 처리할 수 있는 job들을 최대한 워커에 할당하고, 여기까지의 모든 새 state를 반환합니다.
  Input: {:graph {A #{C}, B #{A}, C #{}, D #{A}, E #{B D F}, F #{C}}
          :workers {C 1}
          :result ()}
  Output: {:graph {A #{}, B #{A}, D #{A}, E #{B D F}, F #{}}
           :workers {F 66, A 61}
           :result (C)}"
  [{:keys [graph workers result]}]
  (let [{finished-jobs :finished-jobs
         workers-after-reaping :workers} (->> workers
                                              process-workers-one-step
                                              reap-finished-job)
        new-graph (remove-jobs-from-graph graph finished-jobs)
        new-workers (assign-jobs-to-workers workers-after-reaping new-graph)]
    {:graph new-graph
     :workers new-workers
     :result (concat result finished-jobs)}))
;(process-step-with-workers {:graph {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}}
;                            :workers {}
;                            :result []})

(defn get-result-as-string [{result :removed-keys}]
  (apply str result))

(comment
  ; Part 1
  (->> input-val
       parse
       (#(iterate process-one-step {:removed-keys [] :graph %}))
       (drop-while #(not (empty? (:graph %))))
       first
       get-result-as-string)
  ; Part 2
  (->> input-val
       parse
       (#(iterate process-step-with-workers {:graph % :workers {} :result []}))
       (drop 1)
       (take-while #(or (seq (:workers %)) (seq (:graph %))))
       count))
