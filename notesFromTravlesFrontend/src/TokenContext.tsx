import React, { createContext, useContext, useState, useEffect, ReactNode } from "react";

// Define the types for the context value
interface TokenContextType {
  token: string | null;
  setToken: (token: string | null) => void;
}

// Create the context with the appropriate type
const TokenContext = createContext<TokenContextType | undefined>(undefined);

// TokenProvider to manage token state globally
interface TokenProviderProps {
  children: ReactNode;
}

export const TokenProvider: React.FC<TokenProviderProps> = ({ children }) => {
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const queryParameters = new URLSearchParams(window.location.search);
    const tokenFromUrl = queryParameters.get("token");
    if (tokenFromUrl) {
      setToken(tokenFromUrl);
    }
  }, []);

  return (
    <TokenContext.Provider value={{ token, setToken }}>
      {children}
    </TokenContext.Provider>
  );
};

// Custom hook to use token in components
export const useToken = (): TokenContextType => {
  const context = useContext(TokenContext);
  if (!context) {
    throw new Error("useToken must be used within a TokenProvider");
  }
  return context;
};
