import React, { Suspense } from "react";

export const SuspenseWrapper: React.FC<{ children: React.ReactNode }> = ({ children }) => (
    <Suspense fallback={<div>Loading...</div>}>
        {children}
    </Suspense>
);