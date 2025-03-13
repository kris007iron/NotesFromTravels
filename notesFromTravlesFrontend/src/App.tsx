import React from "react";
import { LoginForm } from "./components/login-form";
import { UserTable } from "./components/users/columns";
import { TokenProvider, useToken } from "./TokenContext"; // Import the TokenProvider

const AppContent: React.FC = () => {
  const { token } = useToken(); // Access the token from the context

  if (token) {
    // Token exists, show the UserTable
    return <UserTable />;
  } else {
    // Token doesn't exist, show the LoginForm
    return (
      <div className="flex flex-col items-center justify-center min-h-svh">
        <LoginForm />
      </div>
    );
  }
};

const App: React.FC = () => {
  return (
    <TokenProvider>
      <AppContent />
    </TokenProvider>
  );
};

export default App;
