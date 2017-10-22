function execute = |event, args| {
    test()

    event: getChannel(): sendMessage("Pong!"): queue()
}