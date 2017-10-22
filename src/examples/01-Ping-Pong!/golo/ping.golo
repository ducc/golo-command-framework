module pingpong

let something = "hello"

function execute = |event, args| {
    event: getChannel(): sendMessage("Pong!"): queue()
}