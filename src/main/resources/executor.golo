module framework.executor

let std = """
# takes the return value and sends it to the discord channel
function textResponse = |f| -> |event, args| {
    let message = f: invoke(event, args)
    event: getChannel(): sendMessage(message): queue()
}

# continues command execution only if the result of the closure is true
function check = |closure| -> |f| -> |event, args| {
    if closure: invoke(event, args) {
        return f: invoke(event, args)
    }
}

# continues command execution only if the user is the guild owner
function ownerOnly = |f| -> |event, args| {
    if event: getMember(): isOwner() {
        return f: invoke(event, args)
    }
}

# continues only if the user is a bot
function botOnly = |f| -> |event, args| {
    if event: getAuthor(): isBot() {
        return f: invoke(event, args)
    }
}

# continues only if the user is not a bot
function userOnly = |f| -> |event, args| {
    if not event: getAuthor(): isBot() {
        return f: invoke(event, args)
    }
}

# continues if the user has the specified role by id
function hasRoleById = |roleId| -> |f| -> |event, args| {
    let role = event: getGuild(): getRoleById(roleId)
    if event: getMember(): getRoles(): contains(role) {
        return f: invoke(event, args)
    }
}

# continues if the user has the specified role by name
function hasRoleByName = |roleName, ignoreCase| -> |f| -> |event, args| {
    foreach role in event: getMember(): getRoles() {
        let name = role: getName()

        if ignoreCase {
            if name: equalsIgnoreCase(roleName) {
                return f: invoke(event, args)
            }
        } else {
            if name: equals(roleName) {
                return f: invoke(event, args)
            }
        }
    }
}
"""

function execute = |source, event, args| {
    # create a new environment to run the command
    let env = gololang.EvaluationEnvironment()

    # prepend the framework std functions
    let code = std + "\n" + source

    # create anonymous module for code
    let mod = env: anonymousModule(code)

    # execute the produced anon module
    let execute = fun("execute", mod)

    try {
        execute(event, args)
    } catch (e) {
        e: printStackTrace()
    }
}