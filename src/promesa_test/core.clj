(ns promesa-test.core
  (:require [promesa.core :as p]))

(comment
  @(future 1)
  (def f1 (future
            (Thread/sleep 10000)
            1))
  2
  @f1

  ;; promise 생성
  @(p/promise 1) ;; resolved

  @(p/promise (Exception. "adsf")) ;; rejected

  (p/deferred) ;; deferred promise

  ;; 이렇게 deferred + future로 비동기 실행 후에 마지막에 resolve!를 쓰는 방법도 있음
  (def deferred (let [v (p/deferred)]
                  (future
                    (println "1")
                    (Thread/sleep 1000)
                    (println "2")
                    (Thread/sleep 1000)
                    (println "3")
                    (Thread/sleep 1000)
                    (println "4")
                    (Thread/sleep 1000)
                    (p/resolve! v "1234"))
                  v))
  @deferred

  ;; javascript style promise 생성
  (p/create (fn [res rej] (res 1)))

  @(-> (p/resolved 1)
       (p/then inc))

  ;; 비동기 job을 프로미스로 만들기 위해선 아래와 같은 패턴이 좋음 (자바스크립트처럼)
  ;; create가 받는 함수 안에서 future로 비동기 작업을 시작하고, 마지막에 res 함수 사용해서 이 프로미스를 resolve한다
  (def p1 (-> (p/create (fn [res rej]
                          (future
                            (Thread/sleep 5000)
                            (res 1)))) ;; 이 시점에서 res 함수는 resolve!처럼 동작함.
              (p/then inc)))
  p1

  (-> (p/rejected (Exception. "error"))
      (p/catch prn))

  (-> (p/create (fn [res rej]
                  (future
                    (Thread/sleep 1000)
                    (let [v (rand-int 2)]
                      (if (= v 1)
                        (res :success)
                        (rej (Exception. "Failed")))))))
      (p/handle (fn [result error]
                  (if error
                    (prn "error:" error)
                    (prn result))
                  :handled)))
  ;; 마지막엔 :handled로 resolve됨. 만약에 handling function이 또 exception을 던진다면 rejected promise가 될듯?

  ;; delay function 써서 특정 시간동안 sleep할 수 있게 간단히 만들 수 있음
  @(-> (p/delay 1000 :result)
       (p/timeout 200 :timeout))

  :rcf)
