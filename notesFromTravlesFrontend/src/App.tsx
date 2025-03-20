import React from "react";
import { LoginForm } from "./components/login-form";
import { UserTable } from "./components/users/columns";
import { TokenProvider, useToken } from "./TokenContext"; // Import the TokenProvider
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Moon, Sun } from "lucide-react";
import { ThemeProvider } from "./components/theme-provider";
import { ModeToggle } from "./components/mode-toggle";

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

const App: React.FC = () => {
  return (
    <TokenProvider>
      <ThemeProvider defaultTheme="system" storageKey="vite-ui-theme">
        <div className="grid grid-cols-2 gap-4 m-4 items-center">
          <div className="flex justify-start pl-10">
            <h1 className="text-3xl font-semibold text-gray-800 dark:text-white">
              Notes From Travel Admin Panel
            </h1>
          </div>
          <div className="flex justify-end">
            <ModeToggle />
          </div>
        </div>
        <div className="relative m-4">
          <AppContent />
        </div>
      </ThemeProvider>
    </TokenProvider>
  );
};

export default App;
