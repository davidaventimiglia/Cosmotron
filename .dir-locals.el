;;; Directory Local Variables
;;; See Info node `(emacs) Directory Variables' for more information.

((nil
  (compile-command . (concat "make -k -C " (let ((l (dir-locals-find-file (or (buffer-file-name) default-directory)))) (if (listp l) (car l) l))))))
