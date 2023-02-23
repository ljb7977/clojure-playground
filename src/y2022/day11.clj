(ns y2022.day11)

(def monkeys
  [{:items [89 73 66 57 64 80]
    :operation #(* % 3)
    :tester [13 6 2]}
   {:items [83, 78, 81, 55, 81, 59, 69]
    :operation #(+ % 1)
    :tester [3 7 4]}
   {:items [76 91 58 85]
    :operation #(* % 13)
    :tester [7 1 4]}
   {:items [71 72 74 76 68]
    :operation #(* % %)
    :tester [2 6 0]}
   {:items [98 85 84]
    :operation #(+ % 7)
    :tester [19 5 7]}
   {:items [78]
    :operation #(+ % 8)
    :tester [5 3 0]}
   {:items [86, 70, 60, 88, 88, 78, 74, 83]
    :operation #(+ % 4)
    :tester [11 1 2]}
   {:items [81 58]
    :operation #(+ % 5)
    :tester [17 3 5]}])

(defn process-monkey [monkeys index reduce-worry-level]
  (let [{:keys [items operation tester]} (nth monkeys index)
        [divider true-dest false-dest] tester
        test #(if (zero? (rem % divider)) true-dest false-dest)
        item-to-next-monkey (->> items
                                 (map operation)
                                 (map reduce-worry-level)
                                 (map (juxt identity test)))
        monkeys (-> monkeys
                    (assoc-in [index :items] [])  ;; clear current monkey's item
                    (update-in [index :count] + (count items)))]
    (reduce (fn [acc [item next-monkey]]
              (update-in acc [next-monkey :items] conj item))
            monkeys
            item-to-next-monkey)))

(defn process-round [reduce-worry-level monkeys]
  (->> (range (count monkeys))
       (reduce (fn [acc index]
                 (process-monkey acc index reduce-worry-level))
               monkeys)))

(comment
  ;; Part 1
  (->> monkeys
       (mapv #(assoc % :count 0))
       (iterate (partial process-round #(quot % 3)))
       (drop 20)
       first
       (sort-by :count >)
       (take 2)
       (map :count)
       (apply *))
  := 119715

  ;; Part 2
  (let [lcm (->> monkeys
                 (map :tester)
                 (map first)
                 (apply *))]
    (->> monkeys
         (mapv #(assoc % :count 0))
         (iterate (partial process-round #(rem % lcm)))
         (drop 10000)
         first
         (sort-by :count >)
         (take 2)
         (map :count)
         (apply *)))
  := 18085004878)
