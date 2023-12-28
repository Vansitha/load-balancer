local SERVER_FILE_NAME = "TestServer"
local SERVER_FILE_DIR = "./test_server/"
local SERVER_FILE_PATH = SERVER_FILE_DIR .. SERVER_FILE_NAME .. ".java"
local BUILD_DIR = SERVER_FILE_DIR .. "build"
local HOSTNAME = "http://localhost/"
local OUTPUT_FILENAME = "server-list.txt"

local function get_server_count()
  if #arg ~= 1 then
    error("Usage: lua test.lua <total-servers>")
  end

  local server_count = tonumber(arg[1]);
  if server_count == nil or server_count <= 0 then
    error("Invalid server instance count. Please provide a positive integer.")
  end

  return server_count
end

local function compile_server()
  local compile_cmd = "javac -d" .. " " .. BUILD_DIR .. " " .. SERVER_FILE_PATH
  print("Executed: " .. compile_cmd)
  local status = os.execute(compile_cmd)
  return status
end

local function get_random_port_num()
  local start_range = 1024
  local end_range = 10000
  return math.random(start_range, end_range)
end

local function start_servers(server_count)
  local active_ports = {}

  for _ = 1, server_count do
    local port = get_random_port_num()
    -- & flag to run process in the background. 
    local start_server_cmd = "java -cp " .. BUILD_DIR .. " " .. SERVER_FILE_NAME .. " " .. port .. " &"
    local isSuccess = os.execute(start_server_cmd)
    print("Executed: " .. start_server_cmd)

    if isSuccess then
      table.insert(active_ports, port)
    end
  end

  print() -- force a new line

  return active_ports
end

local function write_ports_to_file(active_ports)
  local file = io.open(OUTPUT_FILENAME, "w")

  if file == nil then
    return
  end

  for _, port in pairs(active_ports) do
    file:write(HOSTNAME .. tostring(port).."\n")
  end

  file:close()
end

local function run_script()
  -- Used to kill all running server instances. Probably not a good idea to kill all java programs, but it'll will work for now.
  if arg[1] == "--kill" then os.execute("killall java") return end

  local success, server_count = pcall(get_server_count)
  if (not success) then print(server_count) return end

  local isCompiled = compile_server()
  if (not isCompiled) then return end

  local active_ports = start_servers(server_count)

  -- Active ports written to output file for loadbalancer program to use as the input
  write_ports_to_file(active_ports)

end

run_script()
