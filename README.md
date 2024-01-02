# Load Balancer

Built out of curiosity to learn how application load balancers work under the hood.
It's still under development. Just a couple of things fix and iron out.

## Features

- <b>Load Distribution:</b> Uses round-robin method to evenly spread incoming requests across servers.
- <b>Health Checks:</b> Performs regular checks to ensure servers are operational.
- <b>Logging:</b> Maintains logs to track load balancer activities.
- Includes a script for setting up and managing a fleet of servers for testing purposes.

## Load Balancer Setup Guide

Begin by cloning the repository using the following command:

```bash
git clone https://github.com/Vansitha/load-balancer.git
```

After cloning, navigate to the root directory of the cloned repository.

### Prepare the Server Fleet

Within the root directory, locate the `server.lua` file. This script is responsible for creating a fleet of servers.
Before that make sure that you have Lua is installed on your system. Follow
the [installation guide](https://www.lua.org/start.html) for setup instructions. I recommend that you install using
version 5.3.

- **Windows Users**: Run the script with the specified version using:
  ```bash
  lua53 server.lua <number-of-server-instances>
  ```

- **Unix/Mac OS Users**: Execute the script with:
  ```bash
  lua server.lua <number-of-server-instances>
  ```

After execution, the ``server-list.txt`` file will be generated, listing the running backend servers along
with their respective addresses. Port numbers will be automatically assigned to each server. Below is an example
representation of the file content:

```bash
http://localhost:1035
http://localhost:6083
http://localhost:2759
http://localhost:8283
```

### Running the Load Balancer

Now start the load balancer by running the following cmd:

```bash
gradlew run
```

### Test the Configuration

To verify the setup, send a request to the backend server. Open a new terminal session and use the `curl` command:

```bash
curl http://localhost/
```

You should receive a response: `Hello from the backend server`.

Also, you can try testing its concurrent capability by sending multiple concurrent requests.

```bash
curl --parallel --parallel-immediate --parallel-max 3 --config server-list.txt
```

The log file captures all the events that occur, you can check it out for more details.