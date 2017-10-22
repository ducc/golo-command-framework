module checks

function execute = |event, args| {
    event: getChannel(): sendMessage("Pong!"): queue()
}