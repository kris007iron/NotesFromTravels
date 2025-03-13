import { LoginForm } from "./components/login-form"
import { UserTable } from "./components/users/columns"

function App() {
  const queryParameters: URLSearchParams = new URLSearchParams(window.location.search)
  const token: string | null = queryParameters.get("token")
  {if(token) {
    //token verification on backend and getting some data
    
    return (
      <UserTable></UserTable>
    )
  }else
  {
    return (  
      <div className="flex flex-col items-center justify-center min-h-svh">
      <LoginForm></LoginForm>      
      </div>
    )
  }}
  
}

export default App
