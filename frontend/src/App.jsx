import { useEffect, useState } from "react";
import api from "./api";

function App() {

  const [message, setMessage] = useState("Loading...");

  useEffect(() => {

    api.get("/hello")
      .then(res => setMessage(res.data))
      .catch(() => setMessage("Backend Not Reachable"));

  }, []);

  return (
    <div className="container">

      <h1>Banking Application</h1>

      <h2>{message}</h2>

      <button>Login</button>

    </div>
  );
}

export default App;