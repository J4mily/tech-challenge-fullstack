import { ReactNode } from "react";

interface ModalBodyProps {
  children: ReactNode;
  descriptionCupouns: ReactNode;
}

export default function ModalBody({
  children,
}: ModalBodyProps) {
  return (
    <>
      {" "}

      <div className="px-6 pt-4 pb-6">{children}</div>
      
    </>
  );
}
