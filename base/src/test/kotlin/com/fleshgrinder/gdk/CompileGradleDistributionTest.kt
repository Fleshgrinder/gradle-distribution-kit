package com.fleshgrinder.gdk

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@Execution(ExecutionMode.CONCURRENT)
class CompileGradleDistributionTest {
    @Test fun `delete does nothing if directory does not exist`() {
        delete(Jimfs.newFileSystem(Configuration.unix()).getPath("test"))
    }
}
