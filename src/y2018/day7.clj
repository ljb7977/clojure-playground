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
     :removed-keys (first result)}))

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

(defn remove-values
  "맵의 value인 set들에서 특정 value들을 모두 제거합니다.
  Input: {A #{C}, B #{C}, C #{}}, C
  Output: {A #{}, B #{}, C #{}}
  "
  [m values]
  (->> m
       (map (fn [[k v]] [k (apply disj v values)]))
       (into {})))

(defn remove-value
  [m value]
  (remove-values m [value]))

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

; 로직:
; 1. 모든 워커들에 대해 1초식 진행시켜준다.
;worker: {:job \A :remaining 5}
(defn process-workers
  "Input: [{:job A :remaining 5} {:job B :remaining 1}]
  Output: [{:job A :remaining 4} {:job B :remaining 0}]"
  [workers]
  (mapv (fn [{:keys [job remaining]}]
            {:job       job
             :remaining (dec remaining)}) workers))
; ----
(defn get-finished-jobs
  "Input: [{:job A :remaining 0} {:job B :remaining 1}]
  Output: (A)"
  [workers]
  (->> workers
       (filter #(= 0 (:remaining %)))
       (map :job)))

(defn empty-finished-workers
  "Input: [{:job A :remaining 0} {:job B :remaining 1}]
  Output: [{:job B :remaining 1}]"
  [workers]
  (remove (fn [{remaining :remaining}] (= remaining 0)) workers))


; 2. 끝난 워커가 있으면, 그 job을 회수한다. (nil로 변경)
(defn reap-finished-job
  "Input: [{:job A :remaining 0} {:job B :remaining 1}]
  Output: {:workers [{:job B :remaining 1}]
           :finished-jobs [A]}"
  [workers]
  {:workers       (empty-finished-workers workers)
   :finished-jobs (get-finished-jobs workers)})

(defn get-duration-of-job [job]
  (+ 61 (- (int job) (int \A))))

(defn add-job-to-workers [workers job]
  (conj workers {:job job :remaining (get-duration-of-job job)}))

(defn job-already-working? [workers job]
  (->> workers
       (map :job)
       set
       (#(contains? % job))))

(defn assign-jobs-to-workers [workers graph]
  (let [assignable-jobs (->> graph
                             get-keys-where-value-is-empty
                             (sort compare))]
    (reduce (fn [acc val]
              (cond
                (= (count acc) 5) (reduced acc)
                (job-already-working? acc val) acc
                :else (add-job-to-workers acc val)))
            workers
            assignable-jobs)))

; 3. 처리 완료된 알파벳들을 done 리스트에 추가한다.
; 4. 그래프에서 그 알파벳들을 제거한다.
; 5. 넣을 수 있는 만큼 새 알파벳을 워커에 넣는다.
(defn process-step-with-workers [{:keys [graph workers result]}]
  (let [{finished-jobs :finished-jobs
         workers-after-reaping :workers} (->> workers
                                              process-workers
                                              reap-finished-job)
        new-graph (as-> graph v
                      (apply dissoc v finished-jobs)
                      (remove-values v finished-jobs))
        new-workers (assign-jobs-to-workers workers-after-reaping new-graph)]
    {:graph new-graph
     :workers new-workers
     :result (concat result finished-jobs)}))

(process-step-with-workers {:graph {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}}
                            :workers []
                            :result []})
(def graph {\A #{\C}, \B #{\A}, \C #{}, \D #{\A}, \E #{\B \D \F}, \F #{\C}})
(->> []
     process-workers
     reap-finished-job)
(as-> graph v
    (apply dissoc v [])
    (remove-values v []))

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

  (->> input-val
       parse
       (#(iterate process-step-with-workers {:graph % :workers [] :result []}))
       (drop 1)
       (take-while #(or (seq (:workers %)) (seq (:graph %))))
       count))
