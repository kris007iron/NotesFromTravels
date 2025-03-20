import React from "react";
import { LoginForm } from "./components/login-form";
import { UserTable } from "./components/users/columns";
import { TokenProvider, useToken } from "./TokenContext"; // Import the TokenProvider
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Moon, Sun } from "lucide-react";
import { ThemeProvider } from "./components/theme-provider";

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
          <LoginForm /> {/* Your unchanged LoginForm */}
        </CardContent>
      </Card>
    </div>
  );
};

const App: React.FC = () => {
  return (
    <TokenProvider>
      <ThemeProvider>
        <div className="relative m-4">
          <AppContent />
        </div>
      </ThemeProvider>
    </TokenProvider>
  );
};

export default App;
