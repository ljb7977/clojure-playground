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
    {:precond (first precond)
     :result  (first result)}))

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
  (keep (fn [[k v]] (when (empty? v) k)) m))

(defn remove-values
  "맵의 value인 set들에서 특정 value들을 모두 제거합니다.
  Input: {A #{B C}, B #{C}, C #{}}, [B C]
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

(defn get-duration-of-job
  "알파벳 job이 들어오면 그 job을 완료하는데 걸리는 시간을 구합니다.
  Input: A 61
  Output: 61"
  [job offset]
  (+ offset (- (int job) (int \A))))

; 로직:
; workers: 알파벳 -> 남은 시간의 map
; 1. 넣을 수 있는 만큼 새 알파벳을 워커에 넣는다.
(defn assign-jobs-to-workers
  "현재 graph에서 처리할 수 있는 job들을 최대한 worker에 할당합니다."
  [workers graph max-workers duration-offset]
  (let [assignable-jobs (->> graph
                             get-keys-where-value-is-empty
                             (sort compare))]
    (reduce (fn [acc val]
              (cond
                (= (count acc) max-workers) (reduced acc)
                (contains? acc val) acc                     ; already working?
                :else (assoc acc val (get-duration-of-job val duration-offset))))
            workers
            assignable-jobs)))
(assign-jobs-to-workers {} {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}} 5 61)

; 2. 모든 워커들에 대해 1초식 진행시켜준다.
; == (fmap dec workers)
(defn process-workers-one-step
  "Input: {A 5, B 1}
  Output: {A 4, B 0}"
  [workers]
  (reduce-kv (fn [m job remaining] (assoc m job (dec remaining))) {} workers))

; 3. 끝난 워커가 있으면, 그 job을 회수한다.
(defn reap-finished-job
  "Input: {A 0, B 1}
  Output: {:workers {B 1} :finished-jobs (A)}"
  [workers]
  (let [{finished-workers true
         ongoing-workers  false} (group-by #(= 0 (val %)) workers)]
    {:finished-jobs (keys finished-workers)
     :workers       (into {} ongoing-workers)}))

; 4. 처리 완료된 알파벳들을 result 리스트에 추가한다.
; 5. 그래프에서 그 알파벳들을 제거한다.
(defn process-step-with-workers
  "worker들을 이용한 한 스텝을 진행합니다.
  먼저 주어진 workers에서 처리 완료된 job들을 모두 수거하고, finished-jobs에 추가합니다.
  그리고 수거한 job에 해당하는 알파벳들을 그래프에서 제거해 줍니다.
  그 다음엔 새 그래프에서 이제 처리할 수 있는 job들을 최대한 워커에 할당하고, 여기까지의 모든 새 state를 반환합니다.
  Input: {:graph {A #{C}, B #{A}, C #{}, D #{A}, E #{B D F}, F #{C}}
          :workers {C 1}
          :result ()
          :max-workers 5
          :duration-offset 61
          :elapsed-time 0}
  Output: {:graph {A #{}, B #{A}, D #{A}, E #{B D F}, F #{}}
           :workers {F 66, A 61}
           :result (C)
           :max-workers 5
           :duration-offset 61
           :elasped-time 1}"
  [{:keys [graph workers max-workers duration-offset] :as state}]
  (let [{finished-jobs :finished-jobs
         workers'      :workers}
        (-> workers
            (assign-jobs-to-workers graph max-workers duration-offset)
            process-workers-one-step
            reap-finished-job)]
    (-> state
        (assoc :workers workers')
        (update :graph #(remove-jobs-from-graph % finished-jobs))
        (update :elapsed-time inc)
        (update :result #(concat % finished-jobs)))))
;(process-step-with-workers {:graph {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}}
;                            :workers {}
;                            :result []
;                            :max-workers 5
;                            :duration-offset 61
;                            :elapsed-time 0})

(defn init [{:keys [max-workers duration-offset]} graph]
  {:graph graph
   :workers {}
   :result ()
   :elapsed-time 0
   :max-workers max-workers
   :duration-offset duration-offset})

(defn ongoing? [{:keys [workers graph]}]
  (or (seq workers) (seq graph)))

(defn get-result-as-string [{result :result}]
  (apply str result))

(comment
  ; Part 1
  ; Answer: EPWCFXKISTZVJHDGNABLQYMORU
  (->> input-val
       parse
       (init {:max-workers 1 :duration-offset 1})
       (iterate process-step-with-workers)
       (drop-while ongoing?)
       first
       get-result-as-string)
  ; Part 2
  ; Answer: 952
  (->> input-val
       parse
       (init {:max-workers 5 :duration-offset 61})
       (iterate process-step-with-workers)
       (drop-while ongoing?)
       first
       :elapsed-time))
