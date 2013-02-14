gemfire {
    region 'foo', type: 'ONE', {
        attributes expiry: 2
        disk file: 'test'
    }
}