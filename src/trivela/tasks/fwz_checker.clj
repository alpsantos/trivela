(ns trivela.tasks.fwz-checker)

(defn task-fn [config]
  (println "Executing task-fn")
  (Thread/sleep 300))

;(defn make-task2 [{:keys [flag] :as this}]
;  (future
;    (while @flag        ;; ...try/catch
;      (task-fn this))))