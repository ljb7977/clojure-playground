(ns y2018.day5)

(def input-val
  (slurp "resources/y2018/day5.txt"))

(defn abs [n]
  (max n (- n)))

(defn character-matched? [f g]
  (= (abs (- (int f) (int g))) 32))

(defn remove-matching-pairs [stream]
  (reduce (fn [acc val]
            (let [last-char (peek acc)]
              (cond
                (nil? last-char) [val]
                (character-matched? last-char val) (pop acc)
                :else (conj acc val))))
          []
          stream))

(defn loop-removing [s]
  (loop [prev s]
    (let [removed (remove-matching-pairs prev)]
      (if (= removed prev) removed
                           (recur removed)))))

(->> input-val
     loop-removing
     (apply str))
