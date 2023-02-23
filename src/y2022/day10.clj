(ns y2022.day10)

(def input (slurp "src/y2022/day10.txt"))

(defn parse-command [command]
  (let [[command arg] (clojure.string/split command #" ")]
    (case command
      "noop" {:command :noop
              :remaining 1}
      "addx" {:command :addx
              :value (parse-long arg)
              :remaining 2})))

(defn process-command [{:keys [commands x current-cycle current-command]}]
  (let [{:keys [command remaining value]} current-command]
    (case command
      :noop {:commands (rest commands)
             :x x
             :current-cycle (inc current-cycle)
             :current-command (first commands)}
      :addx (case remaining
              2 {:commands commands
                 :x x
                 :current-cycle (inc current-cycle)
                 :current-command (assoc current-command :remaining 1)}
              1 {:commands (rest commands)
                 :x (+ x value)
                 :current-cycle (inc current-cycle)
                 :current-command (first commands)}))))

(comment
  ;; Part 1
  (let [commands (->> (clojure.string/split-lines input)
                      (map parse-command))]
    (->> {:commands (rest commands)
          :x 1
          :current-cycle 1
          :current-command (first commands)}
         (iterate process-command)
         (take 220)
         (filter #(#{20 60 100 140 180 220} (:current-cycle %)))
         (map #(* (:x %) (:current-cycle %)))
         (apply +)))
  := 14220)
