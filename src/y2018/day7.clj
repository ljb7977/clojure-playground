(ns y2018.day7
  (:require util))

(def input-val
  (util/read-lines "y2018/day7-example.txt"))

(defn parse-one-line [s]
  (let [[[_ precond result]] (re-seq #"Step (.) must be finished before step (.) can begin\." s)]
    {:precond (first precond)
     :result (first result)}))

(defn ->graph [xs]
  (let [chars (->> xs
                  (map vals)
                  flatten
                  set)]
    (reduce (fn [acc {:keys [precond result]}]
              (update acc result #(conj % precond)))
            (zipmap chars (repeat #{}))
            xs)))

(defn get-keys-where-value-is-empty-list [m]
  (->> m
       (filter #(empty? (val %)))
       (map key)))

(defn remove-value-from-all-entry [m value]
  (->> m
       (map (fn [[k v]] [k (disj v value)]))
       (into {})))

(defn process-one-step [{:keys [result graph]}]
  (let [keys-with-empty-list (get-keys-where-value-is-empty-list graph)
        min-key-with-empty-list (apply min-key compare keys-with-empty-list)]
    (-> graph
        (dissoc min-key-with-empty-list)
        (remove-value-from-all-entry min-key-with-empty-list)
        (#({:result (conj result min-key-with-empty-list) :graph %})))))

(->> input-val
     (map parse-one-line)
     ->graph
     (#(iterate process-one-step {:result [] :graph %}))
     (take 1))

