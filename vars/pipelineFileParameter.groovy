// Original Author: https://bitbucket.org/janvrany/jenkins-27413-workaround-library/src/default/
// Copyright (c) 2017 Palantir Solutions

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

import hudson.FilePath
import hudson.model.ParametersAction
import hudson.model.FileParameterValue
import hudson.model.Executor

def call(String name, String fname = null) {
    def paramsAction = currentBuild.rawBuild.getAction(ParametersAction.class);
    if (paramsAction != null) {
        for (param in paramsAction.getParameters()) {
            if (param.getName().equals(name)) {
                if (! (param instanceof FileParameterValue)) {
                    error "pipelineFileParameter: not a file parameter: ${name}"
                }
                if (env['NODE_NAME'] == null) {
                    error "pipelineFileParameter: no node in current context"
                }
                if (env['WORKSPACE'] == null) {
                    error "pipelineFileParameter: no workspace in current context"
                }

       if (env['NODE_NAME'].equals("master")) {
           workspace = new FilePath(null, env['WORKSPACE'])
       }else{
                        workspace = new FilePath(Jenkins.getInstance().getComputer(env['NODE_NAME']).getChannel(), env['WORKSPACE'])
       }

                filename = fname == null ? param.getOriginalFileName() : fname
                file = workspace.child(filename)

                destFolder = file.getParent()
                destFolder.mkdirs()

                file.copyFrom(param.getFile())
                return filename;
            }
        }
    }
    error "pipelineFileParameter: No file parameter named '${name}'"
}
