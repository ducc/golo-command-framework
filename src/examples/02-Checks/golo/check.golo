let myCheck = |event, args| {
    return event: getAuthor(): getName(): startsWith("sp")
}

@check(myCheck)
@textResponse
function execute = |event, args| {
    return "Your name starts with sp! :)"
}