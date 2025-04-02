import React from "react";
import { LoginForm } from "./components/login-form";
import { UserTable } from "./components/users/columns";
import { TokenProvider, useToken } from "./TokenContext"; // Import the TokenProvider
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ThemeProvider } from "./components/theme-provider";
import { ModeToggle } from "./components/mode-toggle";
import { Button } from "./components/ui/button";

const AppContent: React.FC = () => {
  const { token } = useToken();

  return token ? (
    <div className="flex flex-col items-center min-h-screen p-6">
      <UserTable />
    </div>
  ) : (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <Card className="w-[350px] shadow-lg">
        <CardHeader>
          <CardTitle>Login</CardTitle>
        </CardHeader>
        <CardContent>
          <LoginForm />
        </CardContent>
      </Card>
    </div>
  );
};



// Extracted header to keep App cleaner
const AppHeader: React.FC = () => {
  const { token, setToken } = useToken(); // Use hook inside a component function

  const logout = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/v1/logout", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        window.location.href = "http://localhost:5173/";
        return;
      }
      setToken(null);
    } catch (error) {
      console.error("❌ Error fetching users:", error);
    }
  };


  return (
    <div className="flex justify-between items-center m-4">
      <h1 className="text-3xl font-semibold text-gray-800 dark:text-white">
        Notes From Travel Admin Panel
      </h1>
      <div className="flex items-center gap-4">
        {token && <Button onClick={() => { logout(); }}>Logout</Button>} {/* Button only shows if token exists */}
        <ModeToggle />
      </div>
    </div>

  );
};

const App: React.FC = () => {
  return (
    <TokenProvider>
      <ThemeProvider defaultTheme="system" storageKey="vite-ui-theme">
        <AppHeader />
        <div className="relative m-4">
          <AppContent />
        </div>
      </ThemeProvider>
    </TokenProvider>
  );
};

export default App;