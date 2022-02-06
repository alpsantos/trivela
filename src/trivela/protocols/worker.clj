(ns trivela.protocols.worker)

(defprotocol IWorker
  (make-task [this])
  (task-fn [this]))