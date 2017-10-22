module framework.executor

let std = """
function test = {
    println("test!")
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