import React from "react";
import { Button } from "@/components/ui/button";
import { useToken } from "@/TokenContext"; // Import the useToken hook

export const LoginForm: React.FC = () => {
  const { setToken } = useToken(); // Access the setter function

  const handleLogin = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    e.preventDefault();
    window.location.href = "http://localhost:8080/oauth2/authorization/github";
  };

  // Handle token update after successful OAuth
  const handleTokenUpdate = (newToken: string) => {
    setToken(newToken); // Update the token in the context
  };

  return (
    <form className="flex flex-col gap-6">
      <div className="flex flex-col items-center gap-2 text-center">
        <h1 className="text-2xl font-bold">Login to admin panel</h1>
      </div>
      <Button variant="outline" onClick={handleLogin}>
        Login with GitHub
      </Button>
    </form>
  );
};
