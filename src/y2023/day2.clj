(ns y2023.day2
  (:require [clojure.string]))

(def sample-input "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\nGame 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\nGame 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\nGame 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\nGame 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")
(def input (slurp "src/y2023/day2.txt"))

(defn parse-set [s]
  (->> (clojure.string/split s #",")
       (map clojure.string/trim)
       (map #(clojure.string/split % #" "))
       (map (fn [[x y]] [(keyword y) (parse-long x)]))
       (into {})))

(defn parse-line [line]
  (let [[game text] (clojure.string/split line #":")
        game-no (parse-long (second (clojure.string/split game #" ")))
        sets (-> text
                 clojure.string/trim 
                 (clojure.string/split #";"))
        sets (map parse-set sets)]
    {:game-no game-no
     :sets sets}))

(defn is-valid? [set-map]
  (and
    (<= (:red set-map 0) 12)
    (<= (:green set-map 0) 13)
    (<= (:blue set-map 0) 14)))


(defn get-max [sets k]
  (->> sets
       (map #(k % 0))
       (apply max)))

(defn get-least-set [sets]
  {:green (get-max sets :green)
   :blue (get-max sets :blue)
   :red (get-max sets :red)})

(comment
  ; Part 1
  (->> (clojure.string/split-lines input)
       (map parse-line)
       (filter (fn [{:keys [sets]}] (every? is-valid? sets)))
       (map :game-no)
       (apply +))

  ; Part 2
  (->> (clojure.string/split-lines input)
       (map parse-line)
       (map (fn [{:keys [sets]}]
              (get-least-set sets)))
       (map vals)
       (map #(apply * %))
       (apply +))
  
  :rcf)
