(ns dev-dive.01-repl-basics)

(comment
  (def data [{:name "Harry Potter" :age 20}
             {:name "Ron Weasley" :age 21}
             {:name "Hermione Granger" :age 22}])

  (/ (->> (map :age data)
          (apply +))
     (count data))

  (def students data)
  (clojure.pprint/pprint students)
  (defn avg-age [students]
    (let [ages (map :age students)
          sum-of-ages (apply + ages)
          avg (/ sum-of-ages (count ages))]
      avg))

  (avg-age students))
